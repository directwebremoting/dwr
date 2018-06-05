package com.omnytex;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
  * This class is a data structure used to pass data from the client to the
  * Freemarker class.
  */
public class FreemarkerData {


  /**
   * The name of the template, fully-qualified, to use to generate output.
   */
  private String template;


  /**
   * Data that will sit directly in the root of the Map passed to Freemarker.
   */
  private Map rootData = new HashMap();


  /**
   * Data that consists of Maps passed to Freemarker.
   */
  private Map mapData = new HashMap();


  /**
   * Mutator for template.
   *
   * @param inTemplate New value for template.
   */
  public void setTemplate(final String inTemplate) {

    template = inTemplate;

  } // End setTemplate().


  /**
   * Accessor for template.
   *
   * @return Value of template.
   */
  public String getTemplate() {

    return template;

  } // End getTemplate().


  /**
   * Mutator for rootData.
   *
   * @param inRootData New value for rootData.
   */
  public void setRootData(final Map inRootData) {

    rootData = inRootData;

  } // End setRootData().


  /**
   * Accessor for rootData.
   *
   * @return Value of rootData.
   */
  public Map getRootData() {

    return rootData;

  } // End getRootData().


  /**
   * Mutator for mapData.
   *
   * @param inMapData New value for mapData.
   */
  public void setMapData(final Map inMapData) {

    mapData = inMapData;

  } // End setMapData().


  /**
   * Accessor for mapData.
   *
   * @return Value of mapData.
   */
  public Map getMapData() {

    return mapData;

  } // End getMapData().


  /**
   * Overriden toString method.
   *
   * @return A reflexively-built string representation of this bean.
   */
  public String toString() {

    String str = null;
    StringBuffer sb = new StringBuffer(1000);
    sb.append("[").append(super.toString()).append("]={");
    boolean firstPropertyDisplayed = false;
    try {
      java.lang.reflect.Field[] fields = this.getClass().getDeclaredFields();
      for (int i = 0; i < fields.length; i++) {
        if (firstPropertyDisplayed) {
          sb.append(", ");
        } else {
          firstPropertyDisplayed = true;
        }
        sb.append(fields[i].getName()).append("=").append(fields[i].get(this));
      }
      sb.append("}");
      str = sb.toString().trim();
    } catch (IllegalAccessException iae) {
      iae.printStackTrace();
    }
    return str;

  } // End toString().


} // End class.
