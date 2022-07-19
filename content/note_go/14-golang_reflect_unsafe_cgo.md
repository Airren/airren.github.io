---
Title: Reflect, Unsafe, and Cgo
---



Reflect

### Reflection Let Us Work with Types at Runtime

But sometimes, relying on only compilation-time information is a limitation. You might need to work with variables at runtime using information that didn't exist when the program was written. Maybe you're trying to map data from a file or network request into a variable, or you want to build a single function that works with different types. In those situations, you need to use `reflection`. Reflection allows us to examine types at runtime. It also provides the ability to examine, modify, and create variables, functions, and structs at runtime.



- Reading and writing from a database. The `database/sql` packages uses reflection to send records to databases and read data back.
- Go's build-in template libraries, `text/template` and `htmp/template`, use reflection to process the values that are passed to the templates.
- The `fmt` package use reflection heavily, as all of those calls to `fmt.Println` and friends rely on reflection to delete the type of the provided parameters.
- The `errors` package uses reflection to implement `errors.Is` and `errors.As`.
- The `sort` package uses reflection to implement functions that sort and evaluate slices of any type: `sort.Slice`,`sort.SliceStable`,`sort.SliceIsSorted`.
- The last main usage of reflection in the Go standard library is for marshaling and unmarshaling data  into JSON and XML, along with the other data formats defined in the various `endoding` packages. Struct tags (which we will talk about soon) are accessed via reflection, and the field in structs are read and written using reflection as well.



Most of these examples have on thing in common: they involve accessing and formatting data that is being imported into or exported out of a Go program. You'll often see reflection used at the boundaries between you program and the outside world.

> `Deepequal` It's in the `reflect` package because it takes advantage of reflection to do its work. The `reflect.Deepequal` function checks to see if two values are "deep equal" to each other. This a more thorough comparison than what you get if you use `==` to compare two things, and it's used in the standard library as a way to validate test results. It can also compare things that can't be compared using `==`, like slices and maps.
>
> Most of the time, you don't need `Deepequal`, but if you ever wanted to compare two maps to see if all of their keys and values are identical or see if two slices were identical, `Deepequal` is what you need. 





### Types, Kinds, and Values



The `reflect` package in the standard library is the home for the types and functions that implement reflections in Go. Reflection is built around three core concepts: types, kinds and values.



First let's look at types. A type in reflection is exactly what it sounds like. It defines the properties of a variable, what it can hold, and how you can interact with it. With reflection, you are able to query a type to find out about these properties using code.



#### Types and kinds

We get the reflection representation of the type a variable with the `Typeof` function in the `reflect` package:

```go
vType := reflect.TypeOf()
```



The `Kind` method on `reflect.Type` returns a value of type `reflect.Kind`, which is a constant that says what the type is made of - a slice, a map, a pointer, a struct, an interface, a string, an array, a function, an int, or some other primitive type. The difference between the kind and the type can be tricky to understand. Remember this rule: if you define a struct named `Foo`, the kind is `reflect.Struct` and the type is "`Foo`".









> FFI: Foreign Function Interface