package com.drofff.palindrome.type;

import java.util.Collection;

public class CollectionPage<T> {

	private Collection<T> collection;

	private int pageNumber;

	private int pageSize;

	public Collection<T> getCollection() {
		return collection;
	}

	public void setCollection(Collection<T> collection) {
		this.collection = collection;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public static class Builder<E> {

		private CollectionPage<E> collectionPage = new CollectionPage<>();

		private Builder() {}

		public static <T> Builder<T> ofCollection(Collection<T> collection) {
			Builder<T> builder = new Builder<>();
			builder.collectionPage.collection = collection;
			return builder;
		}

		public Builder<E> atPage(int pageNumber) {
			collectionPage.pageNumber = pageNumber;
			return this;
		}

		public Builder<E> withPageSize(int pageSize) {
			collectionPage.pageSize = pageSize;
			return this;
		}

		public CollectionPage<E> build() {
			return collectionPage;
		}

	}

}
