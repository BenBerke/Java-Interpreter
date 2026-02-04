# Custom Programming Language Interpreter (Java)

## Overview

This project is a **custom-designed programming language interpreter** implemented in **Java**, created to demonstrate core concepts of programming language design and interpreter architecture.

The language supports variables, control flow, functions, and boolean logic, executing source code through a complete pipeline of **lexing, parsing, and interpretation**.

This project is intended as a **technical skill showcase for CV/portfolio purposes**, not as a production-ready language.

---

## Language Features

### Data Types
- `int`
- `string`
- `bool`

### Variables
- Declaration and assignment
- Type-safe evaluation
- Local and global scope handling

### Control Flow
- `if` statements
- Conditional branching

### Boolean Logic
- Logical operators: `&&`, `||`, `!`
- Comparison operators: `==`, `!=`, `<`, `>`, `<=`, `>=`
- Expression precedence handling

### Functions
- Function definitions
- Parameters
- Local variables
- Independent function scopes
- Return values

### Built-in Functionality
- `print` statement for output

---

## Architecture

The interpreter follows a classic language implementation pipeline:

Source Code
->
Lexer (Tokenization)
->
Parser (AST Generation)
->
Interpreter (AST Evaluation)


### Core Components
- **Lexer** — Converts source code into tokens
- **Parser** — Builds an Abstract Syntax Tree (AST)
- **AST Nodes** — Represent expressions and statements
- **Interpreter** — Executes the AST
- **Environment System** — Manages variable scopes and function contexts

---

## Key Technical Concepts Demonstrated

- Interpreter design and implementation
- Recursive descent parsing
- Abstract Syntax Tree (AST) architecture
- Scope and lifetime management
- Function call stack handling
- Boolean logic evaluation
- Runtime error handling
- Object-oriented design in Java

---

## Example Syntax
This function uses recursion to print the current Fibonacci number and then calls itself with the next two values in the sequence until the specified limit is reached.
```text

var limit = 50;

func fib(a, b) {
    print a;
    var next = a + b;
    if (next >= limit) return;
    fib(b, next);
}

// Start the sequence with 0 and 1
fib(0, 1);
