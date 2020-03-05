package com.drofff.palindrome.mapper;

import static com.drofff.palindrome.utils.DateUtils.dateTimeToStr;

import java.time.LocalDateTime;

import org.modelmapper.Converter;
import org.modelmapper.ExpressionMap;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.drofff.palindrome.document.Violation;
import com.drofff.palindrome.dto.PaymentViolationDto;

@Component
public class PaymentViolationDtoMapper extends Mapper<Violation, PaymentViolationDto> {

	private final TypeMap<Violation, PaymentViolationDto> toDtoTypeMap;

	@Autowired
	public PaymentViolationDtoMapper(ModelMapper modelMapper) {
		super(modelMapper);
		toDtoTypeMap = modelMapper.createTypeMap(Violation.class, PaymentViolationDto.class)
				.addMappings(dateTimeToStrMapping());
	}

	private ExpressionMap<Violation, PaymentViolationDto> dateTimeToStrMapping() {
		return mapping -> mapping.using(dateTimeToStrConverter())
				.map(Violation::getDateTime, PaymentViolationDto::setDateTime);
	}

	private Converter<LocalDateTime, String> dateTimeToStrConverter() {
		return context -> dateTimeToStr(context.getSource());
	}

	@Override
	public PaymentViolationDto toDto(Violation entity) {
		return toDtoTypeMap.map(entity);
	}

}
