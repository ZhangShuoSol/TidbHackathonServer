package com.tidbhack.backend.domain;

/**
 * Created by wenbinsong on 2018/11/30.
 */
public class ExplainParser {
    private Node root;
    private Node current;
    private Integer layer;

    public ExplainParser() {
        layer = 0;
        root = null;
        current = null;
    }

    public void HandleNode(Node newNode) {
        if (newNode.getLayer() == 0) {
            this.layer = 0;
            newNode.setParent(null);
            this.root = newNode;
            this.current = newNode;
            return;
        }

        if (this.layer < newNode.getLayer()) {
            newNode.setParent(this.current);
            this.current.getNodes().add(newNode);
            this.current = newNode;
            this.layer = newNode.getLayer();
        } else {
            int loop = this.layer - newNode.getLayer() + 1;
            for (int i = 0; i < loop; i ++ ) {
                this.current = this.current.getParent();
            }
            newNode.setParent(this.current);
            this.current.getNodes().add(newNode);
            this.layer = newNode.getLayer();
            this.current = newNode;
        }
    }

    public Node getRoot() {
        return this.root;
    }
}
