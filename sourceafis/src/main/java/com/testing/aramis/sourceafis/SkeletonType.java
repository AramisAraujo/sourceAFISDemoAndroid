// Part of SourceAFIS: https://sourceafis.machinezoo.com
package com.testing.aramis.sourceafis;

enum SkeletonType {
	RIDGES("ridges-"), VALLEYS("valleys-");
	final String prefix;
	SkeletonType(String prefix) {
		this.prefix = prefix;
	}
}
