package org.mm.renderer.internal;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class IndividualName extends ReferencedValue {

   public IndividualName(String prefixedName) {
      super(prefixedName);
   }

   @Override
   public boolean equals(Object o) {
      if (o == null) {
         return false;
      }
      if (this == o) {
         return true;
      }
      if (!(o instanceof IndividualName)) {
         return false;
      }
      IndividualName other = (IndividualName) o;
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
