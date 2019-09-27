package org.mm.directive;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

import org.mm.parser.MappingMasterParserConstants;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class ReferenceDirectives implements MappingMasterParserConstants {

   private final String prefix;
   private final String namespace;
   private final String language;

   private final int entityType;
   private final int propertyType;

   private final int valueDatatype;

   private final int valueEncoding;
   private final int iriEncoding;

   private final int shiftDirection;

   private final String labelValue;
   private final String literalValue;

   private final int orderIfCellEmpty;
   private final int orderIfEntityAbsent;

   public ReferenceDirectives(@Nonnull String prefix, @Nonnull String namespace,
         @Nonnull String language, @Nonnull String labelValue, @Nonnull String literalValue,
         int entityType, int propertyType, int valueDatatype, int valueEncoding,
         int iriEncoding, int shiftDirection, int orderIfCellEmpty, int orderIfEntityAbsent) {
      this.prefix = checkNotNull(prefix);
      this.namespace = checkNotNull(namespace);
      this.language = checkNotNull(language);
      this.labelValue = checkNotNull(labelValue);
      this.literalValue = checkNotNull(literalValue);
      this.entityType = entityType;
      this.propertyType = propertyType;
      this.valueDatatype = valueDatatype;
      this.valueEncoding = valueEncoding;
      this.iriEncoding = iriEncoding;
      this.shiftDirection = shiftDirection;
      this.orderIfCellEmpty = orderIfCellEmpty;
      this.orderIfEntityAbsent = orderIfEntityAbsent;
   }

   public String getPrefix() {
      return prefix;
   }

   public boolean useUserPrefix() {
      return prefix.isEmpty() ? false : true;
   }

   public String getNamespace() {
      return namespace;
   }

   public boolean useUserNamespace() {
      return namespace.isEmpty() ? false : true;
   }

   public String getLanguage() {
      return language;
   }

   public boolean useUserLanguage() {
      return language.isEmpty() ? false : true;
   }

   public String getLabelValue() {
      return labelValue;
   }

   public boolean useUserLabelValue() {
      return labelValue.isEmpty() ? false : true;
   }

   public String getLiteralValue() {
      return literalValue;
   }

   public boolean useUserLiteralValue() {
      return literalValue.isEmpty() ? false : true;
   }

   public int getEntityType() {
      return entityType;
   }

   public int getPropertyType() {
      return propertyType;
   }

   public int getValueDatatype() {
      return valueDatatype;
   }

   public int getValueEncoding() {
      return valueEncoding;
   }

   public int getIriEncoding() {
      return iriEncoding;
   }

   public int getShiftDirection() {
      return shiftDirection;
   }

   public int getOrderIfCellEmpty() {
      return orderIfCellEmpty;
   }

   public int getOrderIfEntityAbsent() {
      return orderIfEntityAbsent;
   }
}
