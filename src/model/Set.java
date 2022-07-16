package model;

import java.io.Serializable;
import java.util.Random;

import Exepction.BadInputException;

@SuppressWarnings("serial")
public class Set<T> implements Serializable {

	private final int STARTER_ELEMENT_NUMBER = 2;
	private T[] arr;
	private int numberOfElements;

	@SuppressWarnings("unchecked")
	public Set() {
		if (this.getClass().getName() == "project.OpenQuestion") {
			this.arr = (T[]) new Object[1];
			this.numberOfElements = 0;
		} else {
			this.arr = (T[]) new Object[STARTER_ELEMENT_NUMBER];
			this.numberOfElements = 0;
		}
	}

	@SuppressWarnings("unchecked")
	public Set<T> getRandomElement(int amount) throws BadInputException {
		Set<T> newSet = new Set<T>();
		T[] randomSet = (T[]) new Object[4];
		newSet.setArray(randomSet);
		Random random = new Random();
		for (int i = 0; i < 4; i++) {
			int index = random.nextInt(this.getNumberOfElement());
			if (index == numberOfElements) {
				if (!newSet.contains(this.arr[arr.length - 1]))
					newSet.add(this.arr[arr.length - 1]);
				else
					--i;
			}
			if (!newSet.contains(this.arr[index]))
				newSet.add(this.arr[index]);
			else
				--i;
		}
		return newSet;

	}

	public void setArray(T[] array) {
		this.arr = array;
	}

	@SuppressWarnings("unchecked")
	public Set(Set<T> value) throws BadInputException {
		if (value == null)
			throw new BadInputException("Answer already exists. ");
		if (value.getCurrectSize() == 1) {
			this.arr = (T[]) new Object[1];
			this.arr[0] = value.get(0);
			this.numberOfElements = 1;
		} else {
			this.arr = (T[]) new Object[value.size()];
			for (int i = 0; i < arr.length; i++) {
				this.arr[i] = value.get(i);
			}
			this.numberOfElements = getNumberOfElement();
		}
	}

	public int getNumberOfElement() {
		int counter = 0;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] != null)
				counter++;
		}
		return counter;
	}

	public T firstElement() {
		if (arr[0] != null)
			return this.arr[0];
		else {
			for (int i = 0; i < arr.length; i++) {
				if (arr[i] != null)
					return arr[i];
			}
		}
		return null;
	}

	public T lastElement() {
		if (arr[numberOfElements - 1] != null)
			return this.arr[numberOfElements - 1];
		return null;
	}

	public int size() {
		return this.arr.length;
	}

	public boolean isEmpty() {
		return numberOfElements == -1;
	}

	public int getCurrectSize() {
		return this.numberOfElements;
	}

	public T get(int index) {
		if (index > this.numberOfElements)
			return null;
		return arr[index];
	}

	public T get(T value) {

		for (int i = 0; i < arr.length; i++) {
			if (value.equals(arr[i]))
				return arr[i];
		}
		return null;
	}

	public int capacity() {
		return arr.length;
	}

	public int getMyIndex(T value) {
		for (int i = 0; i < arr.length; i++) {
			if (value == arr[i])
				return i;
		}
		return -1;
	}

	public int getMyIndex(String value) {
		for (int i = 0; i < arr.length; i++) {
			if (((Answer) arr[i]).getText() == value)
				return i;
		}
		return -1;
	}

	public void removeNullObjects() {
		int counter = 0;
		@SuppressWarnings("unchecked")
		T[] newPossibleAnswers = (T[]) new Object[this.numberOfElements];
		for (int i = 0; i < this.numberOfElements; i++) {
			if (arr[i] != null) {
				newPossibleAnswers[counter] = arr[i];
				counter++;
			}
		}
		this.arr = newPossibleAnswers;

	}

	public void orderMyAnswes() {
		removeNullObjects();
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j + i < ((arr.length - 1)); j++) {
				boolean sorted = false;
				for (int k = 0; !sorted && k < arr.length; k++) {
					if (arr[j + 1] != null) {
						// System.out.println("Checking " + arr[j] + " to " + " " + arr[j + 1]);
						if (((Answer) arr[j]).getText().length() == ((Answer) arr[j + 1]).getText().length()) {
							sorted = true;
							continue;
						}
						if (((Answer) arr[j]).getText().length() > ((Answer) arr[j + 1]).getText().length()) {
							sorted = true;
							T temp = arr[j];
							arr[j] = arr[j + 1];
							arr[j + 1] = temp;
						}
					}
				}
			}
		}
	}

	public void add(T value) throws BadInputException {
		if (arr[arr.length - 1] != null)
			increaseArray();
		if (alreadyExist(value))
			throw new BadInputException("Object already exists. " + (value));
		else {

			this.arr[numberOfElements] = value;
			this.numberOfElements++;
			orderMyAnswes();
		}
	}

	@SuppressWarnings("unchecked")
	public T getMyValues() {
		return (T) this.arr;
	}

	public int length() {
		return this.arr.length;
	}

	public boolean contains(T value) {
		if (isEmpty())
			return false;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] != null)
				if (this.equals(value))
					return true;
		}

		return false;
	}

	public boolean equals(Object value) {
		if (value instanceof Answer) {
			Answer myanswer = (Answer) value;
			for (int i = 0; i < arr.length; i++) {
				if (arr[i] != null)
					if (((Answer) arr[i]).getText().equals(myanswer.getText()))
						return true;
			}
		}

		return false;
	}

	public boolean alreadyExist(T value) {
		for (int i = 0; i < arr.length; i++) {
			if (value == arr[i])
				return true;
		}
		return false;
	}

	public T elementAt(int value) {

		if (value > this.numberOfElements)
			return null;
		if (arr[value] != null)
			return this.arr[value];
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	public void increaseArray() {
		T[] tempArray = (T[]) new Object[this.arr.length * 2];
		System.arraycopy(arr, 0, tempArray, 0, arr.length);
		this.arr = tempArray;

	}

	public void deleteObject(int index) throws BadInputException {
		int putIndex = 0;
		if (this.arr.length >= index && index >= 0) {
			@SuppressWarnings("unchecked")
			T[] newPossibleAnswers = (T[]) new Object[this.arr.length - 1];
			for (int i = 0; i < newPossibleAnswers.length+1; ++i) {
				if (i == index) {
					continue;
				} else {
					newPossibleAnswers[putIndex] = this.arr[i];
					putIndex++;

				}
			}
			this.arr = newPossibleAnswers;
		} else {
			throw new BadInputException();
		}
	}

	public String toString() {
		if (isEmpty())
			return "[]";

		String s = "";
		for (int i = 0; i < numberOfElements; i++) {
			s += arr[i] + "\n";
		}
		return s + "";
	}
}
