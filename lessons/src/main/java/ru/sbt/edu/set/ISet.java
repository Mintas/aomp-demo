package ru.sbt.edu.set;

//Disclaimer:
//Our implementations will not use inheritance to let details be as clear as possible
public interface ISet<T> {
    /**
     * Adds the specified element to this set if it is not already present
     * (optional operation).  More formally, adds the specified element
     * {@code e} to this set if the set contains no element {@code e2}
     * such that
     * {@code Objects.equals(e, e2)}.
     * If this set already contains the element, the call leaves the set
     * unchanged and returns {@code false}.  In combination with the
     * restriction on constructors, this ensures that sets never contain
     * duplicate elements.
     *
     * @param element object to be added to this set
     * @return {@code true} if this set did not already contain the specified
     *         element
     */
    boolean add(T element);

    /**
     * Removes the specified element from this set if it is present
     * (optional operation).  More formally, removes an element {@code e}
     * such that
     * {@code Objects.equals(o, e)}, if
     * this set contains such an element.  Returns {@code true} if this set
     * contained the element (or equivalently, if this set changed as a
     * result of the call).  (This set will not contain the element once the
     * call returns.)
     *
     * @param element object to be removed from this set, if present
     * @return {@code true} if this set contained the specified element
     */
    boolean remove(T element);

    /**
     * Returns {@code true} if this set contains the specified element.
     * More formally, returns {@code true} if and only if this set
     * contains an element {@code e} such that
     * {@code Objects.equals(o, e)}.
     *
     * @param element object whose presence in this set is to be tested
     * @return {@code true} if this set contains the specified element
     */
    boolean contains(T element);
}
