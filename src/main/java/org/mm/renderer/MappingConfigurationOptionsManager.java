package org.mm.renderer;

import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParserUtil;
import org.mm.renderer.owlapi.OWLAPIRenderer;

import java.util.HashSet;
import java.util.Set;

public class MappingConfigurationOptionsManager implements MappingMasterParserConstants
{
  private OWLAPIRenderer renderer;

  public MappingConfigurationOptionsManager(OWLAPIRenderer renderer)
  {
    this.renderer = renderer;
  }

  public String getDefaultNameEncoding()
  {
    return getSettingName(renderer.defaultValueEncoding);
  }

  public String getDefaultEntityType()
  {
    return getSettingName(renderer.defaultOWLEntityType);
  }

  public String getDefaultPropertyType()
  {
    return getSettingName(renderer.defaultOWLPropertyType);
  }

  public String getDefaultPropertyValueType()
  {
    return getSettingName(renderer.defaultOWLPropertyValueType);
  }

  public String getDefaultDataPropertyValueType()
  {
    return getSettingName(renderer.defaultOWLDataPropertyValueType);
  }

  public String getDefaultValueEncodingOptionName()
  {
    return getOptionName(MM_DEFAULT_VALUE_ENCODING);
  }

  public String getDefaultEntityTypeOptionName()
  {
    return getOptionName(MM_DEFAULT_ENTITY_TYPE);
  }

  public String getDefaultPropertyTypeOptionName()
  {
    return getOptionName(MM_DEFAULT_PROPERTY_TYPE);
  }

  public String getDefaultPropertyValueTypeOptionName()
  {
    return getOptionName(MM_DEFAULT_PROPERTY_VALUE_TYPE);
  }

  public String getDefaultDataPropertyValueTypeOptionName()
  {
    return getOptionName(MM_DEFAULT_DATA_PROPERTY_VALUE_TYPE);
  }

  public String getMappingConfigurationOption(String optionName)
  {
    int optionID = getOptionID(optionName);

    if (optionID == MM_DEFAULT_VALUE_ENCODING)
      return getSettingName(renderer.defaultValueEncoding);
    else if (optionID == MM_DEFAULT_ENTITY_TYPE)
      return getSettingName(renderer.defaultOWLEntityType);
    else if (optionID == MM_DEFAULT_PROPERTY_TYPE)
      return getSettingName(renderer.defaultOWLPropertyType);
    else if (optionID == MM_DEFAULT_PROPERTY_VALUE_TYPE)
      return getSettingName(renderer.defaultOWLPropertyValueType);
    else if (optionID == MM_DEFAULT_DATA_PROPERTY_VALUE_TYPE)
      return getSettingName(renderer.defaultOWLDataPropertyValueType);
    else
      return "unknown option: " + optionName;
  }

  // TODO: test for valid settings for each option: checkOption
  public void setMappingConfigurationOption(String optionName, String settingName)
  {
    int settingID = getSettingID(settingName);
    int optionID = getOptionID(optionName);

    System.err.println(
      "MappingConfigurationOptionsManager.setMappingConfigurationOption: optionName: " + optionName + ", settingName: "
        + settingName);

    if (settingID != -1) {
      if (optionID == MM_DEFAULT_VALUE_ENCODING)
        renderer.defaultValueEncoding = settingID;
      else if (optionID == MM_DEFAULT_ENTITY_TYPE)
        renderer.defaultOWLEntityType = settingID;
      else if (optionID == MM_DEFAULT_PROPERTY_TYPE)
        renderer.defaultOWLPropertyType = settingID;
      else if (optionID == MM_DEFAULT_PROPERTY_VALUE_TYPE)
        renderer.defaultOWLPropertyValueType = settingID;
      else if (optionID == MM_DEFAULT_DATA_PROPERTY_VALUE_TYPE)
        renderer.defaultOWLDataPropertyValueType = settingID;
    }
  }

  public Set<String> getNameEncodings()
  {
    Set<String> nameEncodings = new HashSet<>();

    for (int i = 0; i < OWLAPIRenderer.NameEncodings.length; i++) {
      nameEncodings.add(ParserUtil.getTokenName(OWLAPIRenderer.NameEncodings[i]));
    }
    return nameEncodings;
  }

  public Set<String> getReferenceValueTypes()
  {
    Set<String> referenceValueTypes = new HashSet<String>();

    for (int i = 0; i < OWLAPIRenderer.ReferenceValueTypes.length; i++) {
      referenceValueTypes.add(ParserUtil.getTokenName(OWLAPIRenderer.ReferenceValueTypes[i]));
    }

    return referenceValueTypes;
  }

  public Set<String> getPropertyTypes()
  {
    Set<String> propertyTypes = new HashSet<String>();

    for (int i = 0; i < OWLAPIRenderer.PropertyTypes.length; i++) {
      propertyTypes.add(ParserUtil.getTokenName(OWLAPIRenderer.PropertyTypes[i]));
    }

    return propertyTypes;
  }

  public Set<String> getPropertyValueTypes()
  {
    Set<String> propertyValueTypes = new HashSet<String>();

    for (int i = 0; i < OWLAPIRenderer.PropertyValueTypes.length; i++) {
      propertyValueTypes.add(ParserUtil.getTokenName(OWLAPIRenderer.PropertyValueTypes[i]));
    }

    return propertyValueTypes;
  }

  public Set<String> getDataPropertyValueTypes()
  {
    Set<String> dataPropertyValueTypes = new HashSet<String>();

    for (int i = 0; i < OWLAPIRenderer.DataPropertyValueTypes.length; i++) {
      dataPropertyValueTypes.add(ParserUtil.getTokenName(OWLAPIRenderer.DataPropertyValueTypes[i]));
    }

    return dataPropertyValueTypes;
  }

  private String getOptionName(int optionID)
  {
    return ParserUtil.getTokenName(optionID);
  }

  private int getSettingID(String settingName)
  {
    return ParserUtil.getTokenID(settingName);
  }

  private int getOptionID(String optionName)
  {
    return ParserUtil.getTokenID(optionName);
  }

  private String getSettingName(int settingID)
  {
    return ParserUtil.getTokenName(settingID);
  }
}