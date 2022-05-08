-- *************************************************************
-- *
-- * 16521724: Jingyu LUO
-- *
-- *************************************************************
{
module Main where

import Data.Char
import System.Environment
import FP_Scanner
}


%name interpreter
%tokentype { Token }

%token
    double  { T_Double $$ }
    ident   { T_Id $$ }
    '+'     { T_Plus }
    '-'     { T_Minus }
    '*'     { T_Times }
    '/'     { T_Divide }
    '('     { T_LeftPar }
    ')'     { T_RightPar }
    '='     { T_Assign }
    let     { T_Let }
    in      { T_In }
    if      { T_If }
    then    { T_Then }
    else    { T_Else }
    sin     { T_Sin }
    cos     { T_Cos }
    exp     { T_Exp }
    '<'     { T_Less }
    '>'     { T_Greater }
    '=='    { T_Equal }
    '\\'    { T_Not }
    '&&'    { T_And }
    '||'    { T_Or }

%right in else
%left '||'
%left '&&'
%left '+' '-'
%left '*' '/'
%right '\\'


%%

txl_program :: { Double }
txl_program : dexp      { $1 (\_ -> error "undefined variable")}

dexp :: { Env -> Double }
dexp :  dexp '+' dexp       { \env -> ($1 env) + ($3 env) }
    | dexp '-' dexp     { \env -> ($1 env) - ($3 env) }
    | dexp '*' dexp     { \env -> ($1 env) * ($3 env) }
    | dexp '/' dexp     { \env -> ($1 env) / ($3 env) }
    | sin '(' dexp ')'  { \env -> sin ($3 env) }
    | cos '(' dexp ')'  { \env -> cos ($3 env) }
    | exp '(' dexp ')'  { \env -> $3 env }  --?????
    | double            { \_ -> $1 }
    | ident             { \env -> env $1 }
    | '-' dexp          { \env -> - ($2 env) }
    | '(' dexp ')'          { $2 }
    | let ident '=' dexp in dexp    { \env -> let v = $4 env
                                              in $6 (\i -> if i == $2
                                                           then v
                                                           else env i)} 
    | if bexp then dexp else dexp   { \env -> if $2 env
                                              then $4 env
                                              else $6 env }

bexp :: { Env -> Bool }
bexp : bexp '&&' bexp   { \env -> ($1 env) && ($3 env) }
    | bexp '||' bexp    { \env -> ($1 env) || ($3 env) }
    | '(' bexp ')'      { $2 }
    | '\\' bexp         { \env -> not ($2 env) }
    | dexp '<' dexp     { \env -> ($1 env) < ($3 env) }
    | dexp '==' dexp    { \env -> ($1 env) == ($3 env) }
    | dexp '>' dexp     { \env -> ($1 env) > ($3 env) }


{
-- Haskell code for defining token type, AST, scanner, top-level
-- functions. 


type Identifier = String
type Env = Identifier -> Double

----------------------------------------------------------------
-- Utilities
----------------------------------------------------------------

happyError :: [Token] -> a
happyError _ = error "Parse error"

----------------------------------------------------------------
-- Main
----------------------------------------------------------------

-- Usage:
--     runtxl file.txl  Interpret "file.txl" and write result
--          to standard output.
--     runtxl       Interpret standard input and write
--          result to standard output.

main = do
    args  <- getArgs
    input <- if null args then getContents else readFile (head args)
    print ((interpreter . alexScanTokens) input)
    print (alexScanTokens input)

}
