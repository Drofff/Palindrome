package com.drofff.palindrome.collector;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import org.springframework.data.util.Pair;

public class PairDequeCollector<T> implements Collector<T, Deque<T>, Deque<Pair<T, T>>> {

	@Override
	public Supplier<Deque<T>> supplier() {
		return ArrayDeque::new;
	}

	@Override
	public BiConsumer<Deque<T>, T> accumulator() {
		return Deque::add;
	}

	@Override
	public BinaryOperator<Deque<T>> combiner() {
		return (deque0, deque1) -> {
			deque0.addAll(deque1);
			return deque0;
		};
	}

	@Override
	public Function<Deque<T>, Deque<Pair<T, T>>> finisher() {
		return this::dequeToPairDeque;
	}

	private Deque<Pair<T, T>> dequeToPairDeque(Deque<T> deque) {
		Deque<Pair<T, T>> pairDeque = new ArrayDeque<>();
		int pairDequeSize = deque.size() / 2;
		for(int i = 0; i < pairDequeSize; i++) {
			Pair<T, T> pair = Pair.of(deque.pop(), deque.pop());
			pairDeque.add(pair);
		}
		return pairDeque;
	}

	@Override
	public Set<Characteristics> characteristics() {
		return Collections.emptySet();
	}

}
