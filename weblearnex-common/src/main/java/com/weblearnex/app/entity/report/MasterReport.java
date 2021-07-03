package com.weblearnex.app.entity.report;

import com.weblearnex.app.constant.ReportCustomType;
import com.weblearnex.app.constant.ReportStatus;
import com.weblearnex.app.constant.ReportType;
import com.weblearnex.app.constant.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "master-report")
public class MasterReport implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "master-report_id", updatable=false, nullable=false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;   // dropdown

    @Enumerated(EnumType.STRING)
    private ReportCustomType reportCustomType;
    private Date fromDate;
    private Date toDate;

    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;
    @ElementCollection
    private List<String> awbList;

    @Enumerated(EnumType.STRING)
    private UserType userType;
    private Date createDate;
    private String createdBy;
}

