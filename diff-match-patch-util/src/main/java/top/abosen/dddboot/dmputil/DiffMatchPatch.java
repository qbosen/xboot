package top.abosen.dddboot.dmputil;


import name.fraser.neil.plaintext.diff_match_patch;

public class DiffMatchPatch {

    public final diff_match_patch dmp = new diff_match_patch();


    public static Builder builder() {
        return new Builder();
    }

    // region builder

    public static final class Builder {
        /**
         * diff 操作允许的最大时间，0表示不限制
         * <p>
         * Number of seconds to map a diff before giving up (0 for infinity).
         */
        private float diffTimeout = 1.0f;
        /**
         * 以编辑字符数为单位的空编辑操作的成本。
         * <p>
         * Cost of an empty edit operation in terms of edit characters.
         */
        private short diffEditCost = 4;
        /**
         * 字符串匹配精度 0.0=精准匹配，1.0=非常松散
         * <p>
         * At what point is no match declared (0.0 = perfection, 1.0 = very loose).
         */
        private float matchThreshold = 0.5f;
        /**
         * 搜索匹配的距离（0=精确位置，1000+=广泛匹配）。
         * 一个与预期位置相差这么多字符的匹配 将增加1.0的分数
         * <p>
         * How far to search for a match (0 = exact location, 1000+ = broad match).
         * A match this many characters away from the expected location will add
         * 1.0 to the score (0.0 is a perfect match).
         */
        private int matchDistance = 1000;
        /**
         * 当删除一个大的文本块（超过~64个字符）时，要多大程度的的内容必须与预期的内容一致。(0.0 = 完美。
         * 1.0 = 非常宽松）。 请注意，{@code matchThreshold} 控制了删除的端点需要多大程度的匹配。
         * <p>
         * When deleting a large block of text (over ~64 characters), how close do
         * the contents have to be to match the expected contents. (0.0 = perfection,
         * 1.0 = very loose).  Note that Match_Threshold controls how closely the
         * end points of a delete need to match.
         */
        private float patchDeleteThreshold = 0.5f;
        /**
         * 语境长度的分块大小。
         * <p>
         * Chunk size for context length.
         */
        private short patchMargin = 4;

        private Builder() {
        }


        /**
         * diff 操作允许的最大时间，0表示不限制
         * <p>
         * Number of seconds to map a diff before giving up (0 for infinity).
         */

        public Builder withDiffTimeout(float diffTimeout) {
            this.diffTimeout = diffTimeout;
            return this;
        }

        /**
         * 以编辑字符数为单位的空编辑操作的成本。
         * <p>
         * Cost of an empty edit operation in terms of edit characters.
         */

        public Builder withDiffEditCost(short diffEditCost) {
            this.diffEditCost = diffEditCost;
            return this;
        }

        /**
         * 字符串匹配精度 0.0=精准匹配，1.0=非常松散
         * <p>
         * At what point is no match declared (0.0 = perfection, 1.0 = very loose).
         */

        public Builder withMatchThreshold(float matchThreshold) {
            this.matchThreshold = matchThreshold;
            return this;
        }

        /**
         * 搜索匹配的距离（0=精确位置，1000+=广泛匹配）。
         * 一个与预期位置相差这么多字符的匹配 将增加1.0的分数
         * <p>
         * How far to search for a match (0 = exact location, 1000+ = broad match).
         * A match this many characters away from the expected location will add
         * 1.0 to the score (0.0 is a perfect match).
         */

        public Builder withMatchDistance(int matchDistance) {
            this.matchDistance = matchDistance;
            return this;
        }

        /**
         * 当删除一个大的文本块（超过~64个字符）时，要多大程度的的内容必须与预期的内容一致。(0.0 = 完美。
         * 1.0 = 非常宽松）。 请注意，{@code matchThreshold} 控制了删除的端点需要多大程度的匹配。
         * <p>
         * When deleting a large block of text (over ~64 characters), how close do
         * the contents have to be to match the expected contents. (0.0 = perfection,
         * 1.0 = very loose).  Note that Match_Threshold controls how closely the
         * end points of a delete need to match.
         */

        public Builder withPatchDeleteThreshold(float patchDeleteThreshold) {
            this.patchDeleteThreshold = patchDeleteThreshold;
            return this;
        }

        /**
         * 语境长度的分块大小。
         * <p>
         * Chunk size for context length.
         */

        public Builder withPatchMargin(short patchMargin) {
            this.patchMargin = patchMargin;
            return this;
        }

        public DiffMatchPatch build() {
            DiffMatchPatch diffMatchPatch = new DiffMatchPatch();
            diffMatchPatch.dmp.Diff_Timeout = diffTimeout;
            diffMatchPatch.dmp.Diff_EditCost = diffEditCost;
            diffMatchPatch.dmp.Match_Threshold = matchThreshold;
            diffMatchPatch.dmp.Match_Distance = matchDistance;
            diffMatchPatch.dmp.Patch_DeleteThreshold = patchDeleteThreshold;
            diffMatchPatch.dmp.Patch_Margin = patchMargin;
            return diffMatchPatch;
        }
    }

    //endregion

    public String diffHtml(String oldText, String newText) {
        StringBuilder html = new StringBuilder();
        for (diff_match_patch.Diff aDiff : dmp.diff_main(oldText, newText, true)) {
            String text = aDiff.text.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\n", "&para;<br>");
            switch (aDiff.operation) {
                case INSERT:
                    html.append("<span class=\"diffinsert\">").append(text)
                            .append("</span>");
                    break;
                case DELETE:
                    html.append("<span class=\"diffdelete\">").append(text)
                            .append("</span>");
                    break;
                case EQUAL:
                    html.append("<span>").append(text).append("</span>");
                    break;
            }
        }
        return html.toString();
    }
}
