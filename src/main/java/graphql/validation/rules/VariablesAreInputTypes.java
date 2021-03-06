package graphql.validation.rules;


import graphql.language.TypeName;
import graphql.language.VariableDefinition;
import graphql.schema.GraphQLType;
import graphql.schema.SchemaUtil;
import graphql.validation.AbstractRule;
import graphql.validation.ValidationContext;
import graphql.validation.ValidationErrorCollector;
import graphql.validation.ValidationErrorType;

public class VariablesAreInputTypes extends AbstractRule {

    private final SchemaUtil schemaUtil = new SchemaUtil();

    public VariablesAreInputTypes(ValidationContext validationContext, ValidationErrorCollector validationErrorCollector) {
        super(validationContext, validationErrorCollector);
    }

    @Override
    public void checkVariableDefinition(VariableDefinition variableDefinition) {
        TypeName unmodifiedAstType = getValidationUtil().getUnmodifiedType(variableDefinition.getType());

        GraphQLType type = getValidationContext().getSchema().getType(unmodifiedAstType.getName());
        if (type == null) return;
        if (!schemaUtil.isInputType(type)) {
            String message = "Wrong type for a variable";
            addError(ValidationErrorType.NonInputTypeOnVariable, variableDefinition.getSourceLocation(), message);
        }
    }
}
