package org.demo.batch.master.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(schema = "demo", name = "TB_CLIENT")
@SequenceGenerator(name = "seqClient",
                   sequenceName = "SEQ_CLIENT",
                   schema = "demo",
                   allocationSize = 1)
@Getter
@Setter
public class Client {

    @Id
    @Column(name = "SEQ_CLIENT")
    @GeneratedValue(generator = "seqClient", strategy = GenerationType.AUTO)
    private Long seqClient;
    @Column(name = "NAME_CLIENT")
    private String name;
    @Column(name = "EMAIL_CLIENT")
    private String email;
}
