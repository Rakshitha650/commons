package io.mosip.kernel.websub.api.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import io.mosip.kernel.websub.api.annotation.PreAuthenticateContentAndVerifyIntent;
import io.mosip.kernel.websub.api.config.IntentVerificationConfig;
import io.mosip.kernel.websub.api.constants.WebSubClientConstants;
import io.mosip.kernel.websub.api.constants.WebSubClientErrorCode;
import io.mosip.kernel.websub.api.exception.WebSubClientException;
import io.mosip.kernel.websub.api.verifier.IntentVerifier;
import io.swagger.models.HttpMethod;
import lombok.Setter;

/**
 * This filter is used for handle intent verification request by hub after subscribe and unsubscribe
 * operations with help of metadata collected by {@link PreAuthenticateContentAndVerifyIntent} and
 * {@link IntentVerificationConfig} class.
 * 
 * @author Urvil Joshi
 *
 */
public class IntentVerificationFilter extends OncePerRequestFilter {

	private IntentVerifier intentVerifier;

	@Setter
	private Map<String, String> mappings = null;

	public IntentVerificationFilter(IntentVerifier intentVerifier) {
		this.intentVerifier = intentVerifier;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (request.getMethod().equals(HttpMethod.GET.name()) && mappings.containsKey(request.getRequestURI())) {
			String topicReq = request.getParameter(WebSubClientConstants.HUB_TOPIC);
			String modeReq = request.getParameter(WebSubClientConstants.HUB_MODE);
			if (intentVerifier.isIntentVerified(mappings.get(request.getRequestURI()),
					request.getParameter("intentMode"), topicReq, modeReq)) {
				response.setStatus(HttpServletResponse.SC_ACCEPTED);
				try {
					response.getWriter().write(request.getParameter(WebSubClientConstants.HUB_CHALLENGE));
					response.getWriter().flush();
					response.getWriter().close();
				} catch (IOException exception) {
					throw new WebSubClientException(WebSubClientErrorCode.IO_ERROR.getErrorCode(),
							WebSubClientErrorCode.IO_ERROR.getErrorMessage().concat(exception.getMessage()));
				}

			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}

		} else {
			filterChain.doFilter(request, response);
		}
	}

}