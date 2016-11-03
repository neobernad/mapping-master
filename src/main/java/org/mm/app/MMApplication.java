package org.mm.app;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;

import javax.annotation.Nonnull;

import org.mm.core.OWLOntologySource;
import org.mm.core.TransformationRuleSet;
import org.mm.core.TransformationRuleSetFactory;
import org.mm.workbook.SpreadSheetDataSource;
import org.mm.workbook.SpreadsheetFactory;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class MMApplication {

   private final OWLOntologySource ontologySource;
   private final SpreadSheetDataSource dataSource;
   private final TransformationRuleSet ruleSet;

   /* package */ MMApplication(@Nonnull OWLOntologySource ontologySource,
         @Nonnull SpreadSheetDataSource dataSource,
         @Nonnull TransformationRuleSet ruleSet) {
      this.ontologySource = checkNotNull(ontologySource);
      this.dataSource = checkNotNull(dataSource);
      this.ruleSet = checkNotNull(ruleSet);
   }

   @Nonnull
   public static MMApplication create(@Nonnull OWLOntologySource ontologySource,
         @Nonnull File workbookFile, @Nonnull File ruleFile) throws Exception {
      checkNotNull(ontologySource);
      checkNotNull(workbookFile);
      checkNotNull(ruleFile);
      return new MMApplication(ontologySource,
            SpreadsheetFactory.loadWorkbookFromDocument(workbookFile),
            TransformationRuleSetFactory.loadTransformationRulesFromDocument(ruleFile));
   }

   @Nonnull
   public static MMApplication create(@Nonnull OWLOntologySource ontologySource,
         @Nonnull File workbookFile) throws Exception {
      checkNotNull(ontologySource);
      checkNotNull(workbookFile);
      return new MMApplication(ontologySource,
            SpreadsheetFactory.loadWorkbookFromDocument(workbookFile),
            TransformationRuleSetFactory.createEmptyTransformationRuleSet());
   }

   @Nonnull
   public MMApplicationModel getApplicationModel() { // TODO: Rename to MappingMasterEngine?
      return new MMApplicationModel(ontologySource, dataSource, ruleSet);
   }
}
