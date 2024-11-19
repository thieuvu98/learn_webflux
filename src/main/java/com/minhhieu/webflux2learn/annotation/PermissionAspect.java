package com.minhhieu.webflux2learn.annotation;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Log4j2
public class PermissionAspect {
    private final ExpressionParser parser = new SpelExpressionParser();

    @Before("@annotation(permission)")
    public void checkPermission(JoinPoint joinPoint, Permission permission) {
        // get signature of method (param types, param values, number of params...)
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // Get method info like return data type, method name,...
//        Method method = signature.getMethod();

        // Create an evaluation context and add method arguments to it
        StandardEvaluationContext context = new StandardEvaluationContext();
        Object[] args = joinPoint.getArgs();
        String[] paramNames = signature.getParameterNames();
        for (int i = 0; i < args.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }

        // Evaluate the SpEL expression
        String subject = evaluate(context, permission.subject());
        String action = evaluate(context, permission.action());
        String object = evaluate(context, permission.object());
        log.info("subjectValue: {}, actionValue: {}, objectValue: {}", subject, action, object);

        // TODO: Auth logic
        // if it is invalid -> throw a Exception

    }

    private String evaluate(StandardEvaluationContext context, String expression) {
        if (!expression.startsWith("#")) {
            return expression;
        }
        return parser.parseExpression(expression).getValue(context, String.class);
    }

}



