package xyz.errorist.versioneer

class BranchInfo {

    String type
    String serie

    BranchInfo(String type, String serie) {
        this.type = type
        this.serie = serie
    }

    static BranchInfo parse(String branch) {
        def type
        def serie
        switch(branch) {
            case ~/release[\/-](.+)/:
                (type, serie) = ['release', branch.substring(8)]
                break
            case ~/feature[\/-](.+)/:
                (type, serie) = ['feature', branch.substring(8)]
                break
            default:
                (type, serie) = [branch, null]
        }
        new BranchInfo(type, serie)
    }

    static BranchInfo parse(String branch, Closure parser) {
        def type
        def serie
        def result = parser ? parser(branch) : null
        if (result == null) {
            return parse(branch)
        } else if (result instanceof List) {
            (type, serie) = [result[0], result[1] ?: branch ]
        } else if (result instanceof String) {
            (type, serie) = [result, branch ]
        } else {
            throw new ParserException("Parser returns wrong type")
        }
        new BranchInfo(type, serie)

    }
}
