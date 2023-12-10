## Project Graph Metrics
Given a `.dot` file, it returns the following graph metrics:
* Indegree
* Outdegree
* Betweenness Centrality
* Height

### Usage

#### Library
Dependency:
```
dependencies {
    implementation("io.github.cdsap:projectgraphmetrics:0.1.0")
}
```
Get metrics:
```
    GraphParser(filePath).getIndicatorsByModule()
```
#### CLI
Example `nowinandroid`
```
./projetGraphMetrics --file=nowinandroid.dot
┌────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                           Top Ten Module Report                                            │
├──────────────────────────┬─────────────────────────┬────────────────────────────┬──────────────────────────┤
│ Indegree                 │ Outdegree               │ BetweennessCentrality      │ Heigt                    │
├─────────────────────┬────┼────────────────────┬────┼────────────────────┬───────┼──────────────────────┬───┤
│ :core:model         │ 16 │ :app               │ 13 │ :core:data         │ 72.0  │ :app                 │ 7 │
├─────────────────────┼────┼────────────────────┼────┼────────────────────┼───────┼──────────────────────┼───┤
│ :core:common        │ 15 │ :feature:search    │ 10 │ :core:datastore    │ 16.5  │ :feature:search      │ 6 │
├─────────────────────┼────┼────────────────────┼────┼────────────────────┼───────┼──────────────────────┼───┤
│ :core:data          │ 12 │ :core:testing      │ 7  │ :core:testing      │ 14.5  │ :sync:sync-test      │ 5 │
├─────────────────────┼────┼────────────────────┼────┼────────────────────┼───────┼──────────────────────┼───┤
│ :core:analytics     │ 11 │ :core:data         │ 7  │ :core:domain       │ 14.0  │ :core:datastore-test │ 5 │
├─────────────────────┼────┼────────────────────┼────┼────────────────────┼───────┼──────────────────────┼───┤
│ :core:designsystem  │ 10 │ :feature:settings  │ 7  │ :core:ui           │ 10.14 │ :core:data-test      │ 5 │
├─────────────────────┼────┼────────────────────┼────┼────────────────────┼───────┼──────────────────────┼───┤
│ :core:domain        │ 8  │ :feature:topic     │ 7  │ :sync:work         │ 3.0   │ :app-nia-catalog     │ 5 │
├─────────────────────┼────┼────────────────────┼────┼────────────────────┼───────┼──────────────────────┼───┤
│ :core:ui            │ 8  │ :feature:bookmarks │ 7  │ :feature:settings  │ 0.14  │ :feature:settings    │ 5 │
├─────────────────────┼────┼────────────────────┼────┼────────────────────┼───────┼──────────────────────┼───┤
│ :core:testing       │ 3  │ :feature:foryou    │ 7  │ :feature:search    │ 0.14  │ :feature:topic       │ 5 │
├─────────────────────┼────┼────────────────────┼────┼────────────────────┼───────┼──────────────────────┼───┤
│ :core:datastore     │ 3  │ :feature:interests │ 7  │ :feature:topic     │ 0.14  │ :feature:bookmarks   │ 5 │
├─────────────────────┼────┼────────────────────┼────┼────────────────────┼───────┼──────────────────────┼───┤
│ :core:notifications │ 2  │ :sync:work         │ 5  │ :feature:bookmarks │ 0.14  │ :feature:foryou      │ 5 │
└─────────────────────┴────┴────────────────────┴────┴────────────────────┴───────┴──────────────────────┴───┘

```
Additionally, the CLI generates the following files:
* modules_report.csv
* modules_report.txt
* top_ten_module_report.txt
##### Getting the CLI
```
 curl -L https://github.com/cdsap/ProjectGraphMetrics/releases/download/v.0.1.0/projectGraphMetrics --output projectGraphMetrics
 chmod 0757 projectGenerator

```

### Generating Dot files
Gradle Plugins generating graph files:

- [jraska/modules-graph-assert](https://github.com/jraska/modules-graph-assert)
  ```bash
  ./gradlew generateModulesGraphvizText -Pmodules.graph.output.gv=all_modules
  ```
- [autonomousapps/dependency-analysis-gradle-plugin](https://github.com/autonomousapps/dependency-analysis-gradle-plugin)

### Libraries used
* clikt
* jgrapht-core
* kotlin-statistics
* picnic
* fatBinary
