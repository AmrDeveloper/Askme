function conditionsValidation(int, def, ...conditions) {
    for (var i = 0; i < conditions.length; i++) {
        if (conditions[i] == false) {
            return def;
        }
    }
    return int;
}

module.exports.conditionsValidation = conditionsValidation;