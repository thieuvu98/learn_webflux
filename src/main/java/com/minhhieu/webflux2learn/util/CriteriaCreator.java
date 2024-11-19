package com.minhhieu.webflux2learn.util;

import org.springframework.data.relational.core.query.Criteria;

public class CriteriaCreator {
    private Criteria condition;

    public CriteriaCreator() {
        this.condition = Criteria.empty();
    }

    public CriteriaCreator and(Criteria criteria) {
        if (criteria != null) {
            condition = condition.and(criteria);
        }
        return this;
    }

    public CriteriaCreator or(Criteria criteria) {
        if (criteria != null) {
            condition = condition.or(criteria);
        }
        return this;
    }

    public Criteria create() {
        return condition;
    }
}
