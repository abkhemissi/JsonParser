import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IntrospectionMain {

	public String dumpObjectToJson(Object obj) throws Exception, IllegalAccessException {
		StringBuilder sb = new StringBuilder();
		
		if (obj == null) {
			sb.append("null");
		} else if (obj instanceof String) {
			sb.append("\"" + obj + "\"");
		} else if (obj instanceof Integer) {
			Integer fieldInt = (Integer) obj;
			int valueInt = fieldInt.intValue();
			sb.append(Integer.toString(valueInt));
		} else if (obj instanceof Collection){
			Collection  ls = (Collection) obj;
			sb.append("[");
			
			for(Iterator<Object> iter = ls.iterator(); iter.hasNext(); ) {
				Object elt = iter.next();
				// *** recursive call ***
				String eltJson = dumpObjectToJson(elt);
				sb.append(eltJson);
				if (iter.hasNext()) {
					sb.append(",");
				}
			}
			sb.append("]");
		} else {
			// case Object ...
			sb.append("{");
			Class<?> clss = obj.getClass();
			Field[] fields = clss.getDeclaredFields();
			for(int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				sb.append("\"" + field.getName() + "\":");
				
				Object fieldValue = extractPropertyValueByFieldOrByGettter(obj, field);
				// *** recursive call ***
				String fieldValueJson = dumpObjectToJson(fieldValue);
				sb.append(fieldValueJson);
				
				if (i + 1 < fields.length) {
					sb.append(", ");
				}
			}
			sb.append("}");
			
		}
		
		return sb.toString();
	}

	private Object extractPropertyValueByFieldOrByGettter(Object obj, Field field) throws IllegalAccessException {
		Object fieldValue;

		int modifiers = field.getModifiers();
        if (Modifier.isPublic(modifiers)) {
			fieldValue = field.get(obj);
		} else {
			// find corresponding getter method, call it
			String methodName = "get" + capitalized(field.getName());
			
			Class<?> clss = obj.getClass();
			Method method;
			try {
				method = clss.getDeclaredMethod(methodName);
			} catch (NoSuchMethodException e) {
				method = null; // method not found?! 
			} catch (SecurityException e) {
				throw new RuntimeException("Should not occur..", e);
			}
			if (method != null) {
				try {
					fieldValue = method.invoke(obj, new Object[0]);
				} catch (IllegalAccessException e) {
					fieldValue = "null"; // private getter method...
				} catch (IllegalArgumentException e) {
					fieldValue = "null"; 
				} catch (InvocationTargetException e) {
					fieldValue = "null"; // getter execution failed ...
				}
			} else {
				fieldValue = "null"; 
			}
		}
			
		return fieldValue;
	}

	private String capitalized(String text) {
		return Character.toUpperCase(text.charAt(0)) + text.substring(1, text.length());
	}
	

	
/*	public <T> T fillObjectFromJson (JsonNode jn, Class<T> classToInstanciateAndFill) throws InstantiationException, IllegalAccessException {	
		//Object classFilled = classToInstanciateAndFill.newInstance();
		Iterator<String> code = jn.fieldNames();
		List<String> ls= new ArrayList<String>();
		
		while (code.hasNext() ){
			 ls.add(code.next());  
		}
		
		Field[] fields = classToInstanciateAndFill.getDeclaredFields();
		for(int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String javaFieldName =field.getType().getName();
			if (javaFieldName=="int") {	
				String fieldInt=ls.get(i);
				int intValue =jn.get(fieldInt).asInt();
				System.out.println(intValue);
			}
			else if (javaFieldName=="java.lang.String") {
				String fieldString=ls.get(i);
				String StringValue = jn.get(fieldString).asText("Default");
				System.out.println(StringValue);
			}
		}
		return null ;//   (T) classFilled ;
	}*/
	
	public <T> T createAndFill(JsonNode jn,Class<T> clazz) throws Exception {
        T instance = clazz.newInstance();
        
        Iterator<String> code = jn.fieldNames();
		List<String> ls= new ArrayList<String>();

		while (code.hasNext() ){
			 ls.add(code.next());  
		}
		int i=0;
		
        for(Field field: clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Object value = getValueForField(field,jn.get(ls.get(i)).asText("Default"),jn);
            field.set(instance, value);
            i++;
        }
        return instance;
    }
	
	private Object getValueForField(Field field, String fieldValue,JsonNode tree) throws Exception {
        Class<?> type = field.getType();
        
        if(type.equals(Integer.TYPE) || type.equals(Integer.class)) {
            return Integer.parseInt(fieldValue);
        } else if(type.equals(Double.TYPE) || type.equals(Double.class)) {
            return Double.parseDouble(fieldValue); 
        } else if(type.equals(String.class)) {
            return fieldValue;
        }  else if (type.equals(ArrayList.class)){
        	ArrayList<Object> ar= new ArrayList<>();
			for(int i=0;i<tree.get(field.getName()).size();i++) {
				ar.add(createAndFill(tree.get(field.getName()).get(i),Car.class));
			}
        	return ar;
        }
        return null;
    }
	

	


}
