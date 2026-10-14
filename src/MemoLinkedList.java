/*
 * Name:        Quang Ho
 * Course:      CS 26000 Data Structures, Fall 2026
 * Project:     Project 2
 * File:        MemoLinkedList.java
 * Date:        10/07/2026
 *
 * Description: A singly linked list of Memo objects with a cursor (current
 *              element). Memos are displayed from the first node to the last.
 *
 * ADT invariant:
 *  1. The memos are stored in a linked list of MemoNode objects.
 *  2. If the list is not empty, head refers to the first node; otherwise null.
 *  3. If the list is not empty, cursor refers to the current node;
 *     otherwise null.
 */

public class MemoLinkedList {

    private MemoNode head;
    private MemoNode cursor;

    /**
     * Creates an empty list.
     */
    public MemoLinkedList() {
        head = null;
        cursor = null;
    }

    // ---------------------------------------------------------------
    // Accessors
    // ---------------------------------------------------------------

    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Returns the first node so the list can be traversed without moving
     * the cursor.
     */
    public MemoNode getHead() {
        return head;
    }

    // ---------------------------------------------------------------
    // Required operations
    // ---------------------------------------------------------------

    /**
     * If a node has the same memoID as newMemo, replace that node's memo.
     * Otherwise insert newMemo after the cursor node (as the first node if
     * the list is empty).
     * Postcondition: newMemo is the current element.
     */
    public void insertAfter(Memo newMemo) {
        if (search(newMemo.getMemoID())) {
            cursor.setData(newMemo);
        } else if (head == null) {
            head = new MemoNode(newMemo, null);
            cursor = head;
        } else {
            cursor.setLink(new MemoNode(newMemo, cursor.getLink()));
            cursor = cursor.getLink();
        }
    }

    /**
     * If a node with memoID == memo_ID exists, move the cursor to that node
     * and return true; otherwise leave the cursor unchanged and return false.
     */
    public boolean search(int memo_ID) {
        for (MemoNode node = head; node != null; node = node.getLink()) {
            if (node.getData().getMemoID() == memo_ID) {
                cursor = node;
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the node whose memoID is memo_ID (does nothing if not found).
     * Postcondition: the cursor refers to the following node, or to head if
     * the removed node was the last one. If the removed node was the only
     * node, the list becomes empty.
     */
    public void remove(int memo_ID) {
        if (!search(memo_ID)) {
            return;
        }

        if (cursor == head) {
            head = head.getLink();
            cursor = head;
        } else {
            MemoNode previous = head;
            while (previous.getLink() != cursor) {
                previous = previous.getLink();
            }
            previous.setLink(cursor.getLink());
            cursor = cursor.getLink();
            if (cursor == null) {
                cursor = head;
            }
        }
    }

    /**
     * Inserts a copy of every memo in other_list into this list, in order,
     * after the cursor. Memos with an existing memoID replace the old ones.
     */
    public void merge(MemoLinkedList other_list) {
        if (other_list == null) {
            return;
        }
        for (MemoNode node = other_list.head; node != null; node = node.getLink()) {
            insertAfter(node.getData().clone());
        }
    }

    /**
     * Returns a copy of the memo at the cursor, or null if the list is empty.
     */
    public Memo retrieve() {
        if (cursor == null) {
            return null;
        }
        return cursor.getData().clone();
    }

    /**
     * Removes all the nodes in the list.
     */
    public void clear() {
        head = null;
        cursor = null;
    }

    // ---------------------------------------------------------------
    // Iterator methods
    // ---------------------------------------------------------------

    public void gotoBeginning() {
        cursor = head;
    }

    public void gotoEnd() {
        if (head == null) {
            return;
        }
        cursor = head;
        while (cursor.getLink() != null) {
            cursor = cursor.getLink();
        }
    }

    /**
     * If the cursor is not at the first node, move it to the prior node and
     * return true; otherwise return false.
     */
    public boolean gotoPrior() {
        if (cursor == null || cursor == head) {
            return false;
        }
        MemoNode previous = head;
        while (previous.getLink() != cursor) {
            previous = previous.getLink();
        }
        cursor = previous;
        return true;
    }

    /**
     * If the cursor is not at the last node, move it to the next node and
     * return true; otherwise return false.
     */
    public boolean gotoNext() {
        if (cursor == null || cursor.getLink() == null) {
            return false;
        }
        cursor = cursor.getLink();
        return true;
    }
}
