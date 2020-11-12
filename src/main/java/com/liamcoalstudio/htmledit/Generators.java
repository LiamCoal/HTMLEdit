package com.liamcoalstudio.htmledit;

public class Generators {
    public static class Generator {
        public final String text, name;
        public final int offset;
        public final boolean group;
        public final Generator[] array;

        public Generator(String name, String text, int offset) {
            this.text = text;
            this.name = name;
            this.offset = offset;
            this.group = false;
            this.array = new Generator[0];
        }

        public Generator(String name, Generator[] array) {
            this.text = "";
            this.name = name;
            this.offset = 0;
            this.group = true;
            this.array = array;
        }
    }

    public static final Generator[] generators = {
            new Generator("Comment", "<!--  -->", 5),
            new Generator("JS and CSS", new Generator[] {
                    new Generator("JavaScript External", "<script src=\"\"/>\n", 13),
                    new Generator("JavaScript Inline", "<script>\n\n</script>\n", 9),
                    new Generator("Stylesheet External", "<link src=\"\" rel=\"stylesheet\">\n", 11),
                    new Generator("Stylesheet Inline", "<style>\n\n</style>\n", 8),
            }),
            new Generator("Headers", new Generator[] {
                    new Generator("H1", "<h1></h1>", 4),
                    new Generator("H2", "<h2></h2>", 4),
                    new Generator("H3", "<h3></h3>", 4),
                    new Generator("H4", "<h4></h4>", 4),
                    new Generator("H5", "<h5></h5>", 4),
                    new Generator("H6", "<h6></h6>", 4),
            })
    };
}
