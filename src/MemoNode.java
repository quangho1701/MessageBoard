/*
 * Name:        Quang Ho
 * Course:      CS 26000-02 Data Structures, Fall 2026
 * Project:     Project 2
 * File:        MemoNode.java
 * Date:        10/07/2026
 *
 * Description: A node of a singly linked list of memos. Each node holds a
 *              Memo ("info") and a reference to the next node ("link").
 */

public class MemoNode {

    private Memo data;
    private MemoNode link;

    public MemoNode(Memo initialData, MemoNode initialLink) {
        data = initialData;
        link = initialLink;
    }

    public Memo getData() {
        return data;
    }

    public MemoNode getLink() {
        return link;
    }

    public void setData(Memo newData) {
        data = newData;
    }

    public void setLink(MemoNode newLink) {
        link = newLink;
    }
}
