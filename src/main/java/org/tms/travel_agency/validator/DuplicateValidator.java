package org.tms.travel_agency.validator;

public interface DuplicateValidator <T>{
    boolean isUnique(T t);
}
