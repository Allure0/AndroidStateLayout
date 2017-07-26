# AndroidStateLayout
 无侵入任一层级添加EmptyView,ErrorView,LoadingView,ContentView

## 效果图

 ![image](https://github.com/Allure0/AndroidStateLayout/tree/master/image/android_state_layout.gif)

### 使用方式
Config in xml:

```
  <com.allure.statelayout.StateLayout
        android:id="@+id/state_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        
    </com.allure.statelayout.StateLayout>
```
### Java调用方法

-  设置EmptyView,ErrorView,LoadingView,ContentView

```java
        stateLayout.setLoadingView();
        stateLayout.setContentView();
        stateLayout.setErrorView();
        stateLayout.setEmptyView();
```
- 切换视图展示View

```java
        stateLayout.showLoading()
        stateLayout.showError();
        stateLayout.showEmpty();
        stateLayout.showContent();
```

- 重写切换动画

```java
stateLayout.setStateChangeListener();

```


## License
Copyright 2017 Allure

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
