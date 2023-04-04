package test;

import top.abosen.xboot.broadcast.BroadcastMessage;

public class Nested {
    @BroadcastMessage("foo")
    public static class Foo {
    }

    @BroadcastMessage("bar")
    public static class Bar {
    }
}