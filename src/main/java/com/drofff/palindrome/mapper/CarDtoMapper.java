package com.drofff.palindrome.mapper;

import static com.drofff.palindrome.utils.DateUtils.parseDateFromStr;

import java.time.LocalDate;
import java.util.Objects;

import org.modelmapper.Converter;
import org.modelmapper.ExpressionMap;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.dto.CarDto;

@Component
public class CarDtoMapper extends Mapper<Car, CarDto> {

	private final TypeMap<CarDto, Car> toEntityTypeMap;

	@Autowired
	public CarDtoMapper(ModelMapper modelMapper) {
		super(modelMapper);
		toEntityTypeMap = modelMapper.createTypeMap(CarDto.class, Car.class)
				.addMappings(dateMapping());
	}

	private ExpressionMap<CarDto, Car> dateMapping() {
		return mapping -> mapping.when(Objects::nonNull)
				.using(strToDateConverter())
				.map(CarDto::getRegistrationDate, Car::setRegistrationDate);
	}

	private Converter<String, LocalDate> strToDateConverter() {
		return context -> parseDateFromStr(context.getSource());
	}

	@Override
	public Car toEntity(CarDto dto) {
		return toEntityTypeMap.map(dto);
	}

}
