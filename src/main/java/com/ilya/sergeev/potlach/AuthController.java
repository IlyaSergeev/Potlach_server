package com.ilya.sergeev.potlach;

import org.springframework.stereotype.Controller;

@Controller
public class AuthController
{
//	POST /oauth/token
//	   - The access point for the OAuth 2.0 Password Grant flow.
//	   - Clients should be able to submit a request with their username, password,
//	      client ID, and client secret, encoded as described in the OAuth lecture
//	      videos.
//	   - The client ID for the Retrofit adapter is "mobile" with an empty password.
//	   - There must be 2 users, whose usernames are "user0" and "admin". All passwords 
//	     should simply be "pass".
//	   - Rather than implementing this from scratch, we suggest reusing the example
//	     configuration from the OAuth 2.0 example in GitHub by copying these classes over:
//	     https://github.com/juleswhite/mobilecloud-14/tree/master/examples/9-VideoServiceWithOauth2/src/main/java/org/magnum/mobilecloud/video/auth
//	     You will need to @Import the OAuth2SecurityConfiguration into your Application or
//	     other configuration class to enable OAuth 2.0. You will also need to remove one
//	     of the containerCustomizer() methods in either OAuth2SecurityConfiguration or
//	     Application (they are the exact same code). You may need to customize the users
//	     in the OAuth2Config constructor or the security applied by the ResourceServer.configure(...) 
//	     method. You should determine what (if any) adaptations are needed by comparing this 
//	     and the test specification against the code in that class.
}
