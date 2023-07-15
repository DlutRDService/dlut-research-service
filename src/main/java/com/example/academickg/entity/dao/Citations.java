package com.example.academickg.entity.dao;

import java.io.Serial;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;

/**
 * <p>
 * 
 * </p>
 *
 * @author zsl
 * @since 2023-05-27
 */
@ApiModel(value = "Citations对象", description = "")
public class Citations implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @TableId(value = "title")
    private String title;

    private String timeSpan;

    private String journal;

    private String conference;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimeSpan() {
        return timeSpan;
    }

    public void setTimeSpan(String timeSpan) {
        this.timeSpan = timeSpan;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getConference() {
        return conference;
    }

    public void setConference(String conference) {
        this.conference = conference;
    }

    @Override
    public String toString() {
        return "Citations{" +
        "title = " + title +
        ", timeSpan = " + timeSpan +
        ", journal = " + journal +
        ", conference = " + conference +
        "}";
    }
}
