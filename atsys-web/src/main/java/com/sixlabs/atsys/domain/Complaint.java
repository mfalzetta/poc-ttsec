package com.sixlabs.atsys.domain;

import javax.persistence.*;

@Entity
@Table(name = "COMPLAINT")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE_ID", discriminatorType = DiscriminatorType.STRING)
public abstract class Complaint extends EntityTimedID {



}
