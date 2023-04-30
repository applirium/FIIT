public class Node {
    private Node leftchild;
    private Node rightchild;
    private final String bfuction;
    Node(String bfuction) {
        this.bfuction = bfuction;
        this.leftchild = null;
        this.rightchild = null;
    }
    public Node getLeftchild() {
        return leftchild;
    }
    public Node getRightchild() {
        return rightchild;
    }
    public String getBfuction() {
        return bfuction;
    }
    public void setLeftchild(Node leftchild) {
        this.leftchild = leftchild;
    }
    public void setRightchild(Node rightchild) {
        this.rightchild = rightchild;
    }
}
