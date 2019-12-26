package tech.rwong.sysu.cloud.server.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import tech.rwong.sysu.cloud.server.misc.ChildrenNodeSerializer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Node {
    public enum NodeType {
        FOLDER, FILE
    };

    @Id // 主键
    @GeneratedValue(strategy = GenerationType.AUTO) // 自增长策略
    private Long id; // 节点的唯一标识

    @Column(nullable = false) // 映射为字段，值不能为空
    private String name;

    @Column(nullable = false)
    private NodeType type;

    @Column(nullable = false)
    private String fullPath;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Node parent;

    @JsonSerialize(using = ChildrenNodeSerializer.class)
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "parent"
    )
    private List<Node> children = new LinkedList<>();

    @JsonIgnore
    @OneToOne(orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "file")
    private Share share;
}
