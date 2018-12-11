package io.mosip.preregistration.batchjobservices.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The Processed PreId List Dto class
 * 
 * @author M1043008
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProcessedPreRegListDto {

	private String prereg_id;
	
	private Timestamp first_received_dtimes;
	
	private String status_code;
	
	private String status_comments;
	
	private String prereg_trn_id;
	
	private String lang_code;
	
	private String cr_by;
	
	private Timestamp cr_dtimes;
	
	private String upd_by;
	
	private Timestamp upd_times;
	
	private boolean is_deleted;
	
	private Timestamp del_dtimes;

	public boolean isIs_deleted() {
		return is_deleted;
	}

	public void setIs_deleted(boolean is_deleted) {
		this.is_deleted = is_deleted;
	}

	
}
