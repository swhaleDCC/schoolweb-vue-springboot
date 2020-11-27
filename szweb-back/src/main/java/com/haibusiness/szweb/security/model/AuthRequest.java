package com.haibusiness.szweb.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Jinyu
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class  AuthRequest {
	
	private String username;
	
	private String password;

}
