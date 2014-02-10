package tzuyu.engine.utils;

import java.util.Arrays;

public class Pair<A, B> {
	public A a;
	public B b;

	public static <A, B> Pair<A, B> of(A a, B b) {
		return new Pair<A, B>(a, b);
	}

	public Pair(A a, B b) {
		this.a = a;
		this.b = b;
	}

	public A first() {
		return a;
	}

	public B second() {
		return b;
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof Pair)) {
			return false;
		}
		Pair<?, ?> that = (Pair<?, ?>)obj;
		return LogicUtils.equalsWithNull(this.a, that.a) && LogicUtils.equalsWithNull(this.b, that.b);
	}
	
	public int hashCode() {
		return Arrays.hashCode(new Object[]{a, b});
	}
	public String toString() {
		return "("+a+", "+b+")";
	}
}
