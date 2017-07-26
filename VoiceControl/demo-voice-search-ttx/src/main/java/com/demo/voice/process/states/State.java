package com.demo.voice.process.states;

import org.apache.commons.lang3.StringUtils;

import com.demo.voice.process.dto.Literal;
import com.demo.voice.process.service.ADService;

public enum State {
	INITIAL {
		@Override
		public State doSomething(Literal result, ADService adService) {
			if (StringUtils.isBlank(result.getConfirmText())) {
				message = "Welcome to IT HELPDESK.";
				return this;
			}
			String name = result.getName() != null ? result.getName() : "";
			message = "Hello " + name
					+ ", Thanks for calling, we will help you reset your account. Please spell your User ID ";
			return VERIFY_USER;
		}
	},
	USER {
		@Override
		public State doSomething(Literal result, ADService adService) {
			message = "Hello " + result.getName()
					+ ", Thanks for calling, we will help you reset your account. Please spell your User ID ";
			return VERIFY_USER;
		}
	},
	VERIFY_USER {
		@Override
		public State doSomething(Literal result, ADService adService) {
			if (StringUtils.isBlank(result.getUserId())) {
				return this;
			}
			String status = adService.checkUserStatus(result.getUserId());
			if ("BLOCKED".equalsIgnoreCase(status)) {
				message = "I notice that your account is locked out.  If you know your password,  I can simply unlock it for you.  Would you like me to unlock it?";
				return State.CONFIRMRESET;
			} else if ("NOT_REGISTER_SELF_SERVICE".equalsIgnoreCase(status)) {
				message = "You have not yet registered for password self-service.  I will send you an email describing how to do this on ce your password has been reset by the TTX Service Desk.  Yo u can contact the TTX Service Desk at 312.984.3709 for assistance";
				return SELFSERVICE;
			} else if ("NOT_FOUND".equalsIgnoreCase(status)) {
				message = "Your userid isn't exist";
				return this;
			} else if ("NORMAL".equalsIgnoreCase(status)) {
				message = "Your account is not locked out .  Do you need me to reset your password?";
				return RESETWAY;
			}
			return this;
		}
	},
	SELFSERVICE {
		@Override
		public State doSomething(Literal result, ADService adService) {
			return INITIAL;
		}

	},
	CONFIRMRESET {

		@Override
		public State doSomething(Literal result, ADService adService) {
			if (result.isConfirm()) {
				message = "Please choose method to verify: SMS, EMAIL, PHONE";
				return RESETWAY;
			}
			message = "Ok,  If you need further assistance,  please contact the TTX Service Desk at 312.984.3709.";
			return INITIAL;
		}

	},
	RESETWAY {

		@Override
		public State doSomething(Literal result, ADService adService) {
			if (StringUtils.isBlank(result.getDelivery())) {
				message = "Sorry, please choose method to verify again.";
				return this;
			}

			if ("sms".equalsIgnoreCase(result.getDelivery())) {
				message = "I just sent a text message to your mobile phone.  Please give me the code provided in that message and I will reset your password ";
			} else if ("email".equalsIgnoreCase(result.getDelivery())) {
				message = "I just sent a email to your email.  Please give me the code provided in that message and I will reset your password ";
			} else if ("phone".equalsIgnoreCase(result.getDelivery())) {
				message = "I will call you on your mobile phone and give you a code.  Please give me the code provided in that message and I will reset your password ";
			}
			return FEEDBACK;
		}

	},
	FEEDBACK {

		@Override
		public State doSomething(Literal result, ADService adService) {
			message = "Were you able to successfully log in ?";
			return FINAL;
		}

	},
	FINAL {

		@Override
		public State doSomething(Literal result, ADService adService) {
			if (result.isConfirm()) {
				message = "END";
			} else {
				message = "Refer back to Service Desk";
			}
			return INITIAL;
		}

	};

	public abstract State doSomething(Literal result, ADService adService);

	private static String message;
	public String getMessage() {
		return message;
	}
	
}
