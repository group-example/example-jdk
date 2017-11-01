package util.concurrency.queue;

import java.lang.management.ThreadInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * Definition for singly-linked list.
 **/
public class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
    }

    public static void main(String[] args) {
        ListNode l1 = new ListNode(8);
        ListNode l2 = new ListNode(9);
        ListNode l3 = new ListNode(9);
        l1.next = l2;
        l2.next = l3;

        ListNode l21 = new ListNode(2);
        ListNode l22 = new ListNode(0);
        ListNode l23 = new ListNode(4);
        l21.next = l22;
//        l22.next = l23;

        ListNode result = Solution.addTwoNumbers(l1, l21);

        do {
            System.out.print(result.val + ",");
            result = result.next;
        }
        while (result != null);
        System.out.println();


    }
}

class Solution {
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        int nextValue = 0;
        List<ListNode> resultList = new ArrayList<>();
        while (l1 != null || l2 != null || nextValue == 1) {

            int value = ((l1 != null) ? l1.val : 0) + ((l2 != null) ? l2.val : 0) + nextValue;
            nextValue = value / 10;
            value = value % 10;

            ListNode result = new ListNode(value);
            resultList.add(result);

            if (l1 != null) {
                l1 = l1.next;
            }
            if (l2 != null) {
                l2 = l2.next;
            }
        }
        for (int j = 0; j < resultList.size(); j++) {
            if (j != resultList.size() - 1) {
                resultList.get(j).next = resultList.get(j + 1);
            }
        }
        return resultList.get(0);
    }
}
