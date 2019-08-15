package org.mm.renderer.internal;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class ObjectPropertyName extends PropertyName {

   public ObjectPropertyName(String prefixedName) {
      super(prefixedName);
   }

   @Override
   public ObjectPropertyName update(String newPrefixedName) {
      return new ObjectPropertyName(newPrefixedName);
   }

   @Override
   public boolean isDataProperty() {
      return false;
   }

   @Override
   public boolean isObjectProperty() {
      return true;
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
      if (!(o instanceof ObjectPropertyName)) {
         return false;
      }
      ObjectPropertyName other = (ObjectPropertyName) o;
      return Objects.equal(this.getString(), other.getString());
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.getString());
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this)
            .addValue(this.getString())
            .toString();
   }
}
