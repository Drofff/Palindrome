package com.drofff.palindrome.enums;

import java.util.function.Function;

import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.service.DriverService;

public enum DriverIdType {

	DRIVER_ID {

		@Override
		public Function<String, Driver> getFindDriverFunctionFromService(DriverService driverService) {
			return driverService::getDriverById;
		}

	}, USER_ID {

		@Override
		public Function<String, Driver> getFindDriverFunctionFromService(DriverService driverService) {
			return driverService::getDriverByUserId;
		}

	};

	public abstract Function<String, Driver> getFindDriverFunctionFromService(DriverService driverService);

}
