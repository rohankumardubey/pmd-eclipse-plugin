/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.eclipse.ui.preferences.br;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.properties.StringProperty;

/**
 * 
 * @author BrianRemedios
 * @deprecated unused and incomplete
 */
@Deprecated
public class FilterManager {

    private String newFilterPrefix;
    private final StringProperty filterPropertyDesc;

    private Map<String, List<Rule>> rulesByFilterName;
    private Map<Integer, String> namesByFilterHash;

    public FilterManager(StringProperty filterDesc, String filterPrefix) {
        filterPropertyDesc = filterDesc;
        newFilterPrefix = filterPrefix;
    }

    public void addFilter(String name, String source) {
        // TODO
    }

    public void addFilterFrom(Rule rule) {

        if (rulesByFilterName == null) {
            rulesByFilterName = new HashMap<>();
            namesByFilterHash = new HashMap<>();
        }

        String filter = rule.getProperty(filterPropertyDesc);
        if (StringUtils.isBlank(filter)) {
            return;
        }

        filter = filter.trim();

        int hashCode = filter.hashCode();
        String name = namesByFilterHash.get(hashCode);
        if (name == null) {
            name = newName();
            namesByFilterHash.put(hashCode, name);
            rulesByFilterName.put(name, new ArrayList<Rule>());
        }

        rulesByFilterName.get(name).add(rule);
    }

    public void setFilter(String name, Rule rule) {
        // TODO
    }

    private String newName() {
        return newFilterPrefix + namesByFilterHash.size();
    }

    public void setName(String name, String filterSource) {
        int code = filterSource.trim().hashCode();
        namesByFilterHash.put(code, name);
    }

    public String[] names() {
        if (rulesByFilterName == null) {
            return new String[0];
        }
        return rulesByFilterName.keySet().toArray(new String[rulesByFilterName.size()]);
    }

    public int referencesTo(String name) {
        if (rulesByFilterName == null) {
            return 0;
        }
        List<Rule> rules = rulesByFilterName.get(name);
        return rules == null ? 0 : rules.size();
    }

    public void modify(String name, String newSource) {

        if (rulesByFilterName == null) {
            return;
        }

        String theSource = newSource.trim();
        int hash = theSource.hashCode();
        namesByFilterHash.put(hash, name);

        for (Rule rule : rulesByFilterName.get(name)) {
            rule.setProperty(filterPropertyDesc, theSource);
        }
    }

    public void rename(String oldName, String newName) {
        // TODO
    }

    public void remove(String name) {
        for (Rule rule : rulesByFilterName.get(name)) {
            rule.setProperty(filterPropertyDesc, null);
        }
        rulesByFilterName.remove(name);

        int hashKey = 0; // do a reverse lookup
        for (Map.Entry<Integer, String> entry : namesByFilterHash.entrySet()) {
            if (entry.getValue().equals(name)) {
                hashKey = entry.getKey();
                break;
            }
        }

        namesByFilterHash.remove(hashKey);
    }

    public void load() {
        // TODO
    }

    public void save() {
        // TODO
    }
}
