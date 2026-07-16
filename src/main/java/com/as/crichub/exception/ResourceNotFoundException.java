package com.as.crichub.exception;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	String resourceNmae;
	String fieldName;
	String fieldValue;
 
	/**
	 * @return the resourceNmae
	 */
	public String getResourceNmae() {
		return resourceNmae;
	}
 
	/**
	 * @param resourceNmae the resourceNmae to set
	 */
	public void setResourceNmae(String resourceNmae) {
		this.resourceNmae = resourceNmae;
	}
 
	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}
 
	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
 
	/**
	 * @return the fieldValue
	 */
	public String getFieldValue() {
		return fieldValue;
	}
 
	/**
	 * @param fieldValue the fieldValue to set
	 */
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
 
	public ResourceNotFoundException(String resourceNmae, String fieldName, String fieldValue) {
		super(String.format("%s not found with this %s :%s", resourceNmae, fieldName, fieldValue));
		this.resourceNmae = resourceNmae;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
 
	public ResourceNotFoundException(String resourceNmae) {
		super(String.format(" Order not found for Order No : %s", resourceNmae));
		this.resourceNmae = resourceNmae;
 
	}
}
