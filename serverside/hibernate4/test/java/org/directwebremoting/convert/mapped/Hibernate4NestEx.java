package org.directwebremoting.convert.mapped;

import org.directwebremoting.util.CompareUtil;

/**
 * A hibernate bean to fit the declaration in
 * {@link org.directwebremoting.hibernate.Database}
 * 
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @author Hacked by Matt Conroy 3/13/2011
 */
public class Hibernate4NestEx implements Comparable<Hibernate4NestEx> {
	private Integer id;
	private String name;
	private Hibernate4Ex owner;
	
	public Hibernate4NestEx() {
	}

	public Hibernate4NestEx(Integer id) {
		this.id = id;
	}

	public Hibernate4NestEx(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Hibernate4NestEx(Integer id, String name, Hibernate4Ex owner) {
		this.id = id;
		this.name = name;
		this.owner = owner;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Hibernate4Ex getOwner() {
		return owner;
	}

	public void setOwner(Hibernate4Ex owner) {
		this.owner = owner;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (owner == null) {
			return "Hibernate4NestEx[id=" + getId() + ",name=" + getName()
					+ ",owner=null]";
		} else {
			return "Hibernate4NestEx[id=" + getId() + ",name=" + getName()
					+ ",owner=" + getOwner().getName() + "]";
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj == this) {
			return true;
		}

		Class<? extends Hibernate4NestEx> thisClass = this.getClass();
		Class<?> thatClass = obj.getClass();
		if (!thisClass.isAssignableFrom(thatClass)
				&& !thatClass.isAssignableFrom(thisClass)) {
			return false;
		}

		Hibernate4NestEx that = (Hibernate4NestEx) obj;

		if (!CompareUtil.equals(this.getId(), that.getId())) {
			return false;
		}

		// Normally .equals should only have to test PK for equality with a DB
		// but we want our tests to be tighter ...

		if (!CompareUtil.equals(this.getName(), that.getName())) {
			return false;
		}

		// We don't want to recurse
		// if (!CompareUtil.equals(this.getOwner(), that.getOwner()))
		// {
		// return false;
		// }

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int reply = 7423;
		reply += (id == null) ? 382 : id.hashCode();
		reply += (name == null) ? 423 : name.hashCode();
		return reply;
	}

	// Implement the comparable interface to allow this object to be stored in
	// a TreeSet.
	public int compareTo(Hibernate4NestEx o) {
		if (this.id > o.getId())
			return 1;
		else if (this.id < o.getId())
			return -1;
		return 0;
	}
}
