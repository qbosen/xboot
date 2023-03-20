package test;

import top.abosen.xboot.broadcast.BroadcastMessage;

public class DuplicateName {
    @BroadcastMessage("foo")
    public static class Foo {
    }

    @BroadcastMessage({"bar", "foo"})
    public static class Bar {
    }
}