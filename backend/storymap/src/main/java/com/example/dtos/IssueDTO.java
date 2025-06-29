package com.example.dtos;

public class IssueDTO {
    public Long id;
    public Long gitlabIssueId;
    public String title;
    public String description;
    public String[] labels;
    public String releaseName;

    public IssueDTO() {

    }

    public IssueDTO(Long gitlabIssueId, String title, String description, String[] labels, String releaseName, Long id) {
        this.gitlabIssueId = gitlabIssueId;
        this.title = title;
        this.description = description;
        this.labels = labels;
        this.releaseName = releaseName;
        this.id = id;
    }
}
