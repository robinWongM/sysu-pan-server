package tech.rwong.sysu.cloud.server.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Part {
    public enum PartStatus {
        UPLOADING, DONE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    private Long id; // Part 的唯一标识

    @Column(nullable = false)
    private PartStatus status;

    private Long size;

    @JsonIdentityReference(alwaysAsId = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upload_id")
    private Upload upload;
}
