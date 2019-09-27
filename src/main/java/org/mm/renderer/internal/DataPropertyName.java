package org.mm.renderer.internal;

import javax.annotation.Nonnull;
import com.google.common.base.Objects;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class DataPropertyName extends PropertyName {

   public DataPropertyName(@Nonnull String value, boolean isFromWorkbook) {
      super(value, isFromWorkbook);
   }

   public static DataPropertyName create(@Nonnull String value) {
      return new DataPropertyName(value, false);
   }

   @Override
   public DataPropertyName update(String newValue) {
      return new DataPropertyName(newValue, isFromWorkbook());
   }

   @Override
   public boolean isDataProperty() {
      return true;
   }

   @Override
   public boolean isObjectProperty() {
      return false;
   }

   @Override
   public boolean isAnnotationProperty() {
      return false;
   }

   @Override
   public boolean equals(Object o) {
      if (o == null) {
         return false;
      }
      if (this == o) {
         return true;
      }
      if (!(o instanceof DataPropertyName)) {
         return false;
      }
      DataPropertyName other = (DataPropertyName) o;
      return Objects.equal(getString(), other.getString())
            && Objects.equal(isFromWorkbook(), other.isFromWorkbook());
   }
}
