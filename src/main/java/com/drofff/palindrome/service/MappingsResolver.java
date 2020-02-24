package com.drofff.palindrome.service;

public interface MappingsResolver {

	<F, T> T resolveMappings(F source, T destination);

}