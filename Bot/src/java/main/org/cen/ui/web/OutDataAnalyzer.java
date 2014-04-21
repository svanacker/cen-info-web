package org.cen.ui.web;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import org.cen.com.out.OutData;
import org.junit.Assert;
import org.scannotation.AnnotationDB;
import org.scannotation.WarUrlFinder;

public class OutDataAnalyzer {
	public class OutDataComparator implements Comparator<Class<OutData>> {
		@Override
		public int compare(Class<OutData> arg1, Class<OutData> arg2) {
			String p1 = getCup(arg1);
			String p2 = getCup(arg2);
			// tri par coupe décroissant
			int result = p2.compareTo(p1);
			if (result == 0) {
				// puis tri alphabétique croissant
				result = arg1.getName().compareTo(arg2.getName());
			}
			return result;
		}
	}

	private Pattern pattern;

	public OutDataAnalyzer() {
		super();
		pattern = Pattern.compile("org\\.cen\\.cup\\.cup([0-9]{4})\\..*");
	}

	@SuppressWarnings("unchecked")
	public Map<Class<OutData>, String> findOutData(ServletContext servletContext) {
		URL[] urls = new URL[] { WarUrlFinder.findWebInfClassesPath(servletContext) };
		Assert.assertNotNull(urls);
		AnnotationDB db = new AnnotationDB();
		try {
			db.scanArchives(urls);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Map<Class<OutData>, String> result = new HashMap<Class<OutData>, String>();

		Map<String, Set<String>> annotationIndex = db.getClassIndex();
		for (Map.Entry<String, Set<String>> entry : annotationIndex.entrySet()) {
			String key = entry.getKey();
			if (key.endsWith("OutData")) {
				try {
					Class<OutData> outDataClass = (Class<OutData>) Class.forName(key);
					if ((outDataClass.getModifiers() & Modifier.ABSTRACT) == 0) {
						Constructor<?> constructor = outDataClass.getConstructors()[0];
						Class<?>[] types = constructor.getParameterTypes();
						Object[] values = new Object[types.length];
						int i = 0;
						for (Class<?> type : types) {
							if (int.class.isAssignableFrom(type)) {
								values[i] = 0;
							} else if (double.class.isAssignableFrom(type)) {
								values[i] = 0d;
							} else if (boolean.class.isAssignableFrom(type)) {
								values[i] = false;
							} else if (String.class.isAssignableFrom(type)) {
								values[i] = "";
							} else if (Enum.class.isAssignableFrom(type)) {
								EnumSet<?> set = EnumSet.allOf((Class<Enum>) type);
								values[i] = set.toArray()[0];
							} else {
								System.err.println("unhandled type: " + type);
							}
							i++;
						}
						OutData outData = (OutData) constructor.newInstance(values);
						String header = outData.getHeader();

						result.put(outDataClass, header);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public String getCup(Class<OutData> data) {
		String name = data.getName();
		Matcher m = pattern.matcher(name);
		if (m.matches()) {
			// année de la coupe pour les données spécifiques
			return m.group(1);
		} else {
			// données génériques
			return "generic";
		}
	}

	public List<Class<OutData>> getSortedList(Map<Class<OutData>, String> map) {
		List<Class<OutData>> list = new ArrayList<Class<OutData>>();
		Set<Class<OutData>> keys = map.keySet();
		for (Class<OutData> key : keys) {
			list.add(key);
		}
		Collections.sort(list, new OutDataComparator());
		return list;
	}
}
