-- *************************************************************
-- *
-- * 16521724: Jingyu LUO
-- *
-- *************************************************************
{
module FP_Scanner where

import Data.Char
import System.Environment
}

%wrapper "basic"

$digit = [0-9]
$alpha = [a-zA-Z]   -- alphabetic characters

@decimal     = $digit+
@exponent    = [eE] [\-\+]? @decimal

@number      = [\-\+]? @decimal
             | [\-\+]? @decimal \. @decimal @exponent?
             | [\-\+]? @decimal @exponent

tokens :-

  $white+       ;
  "!".*         ;
  let         { \s -> T_Let }
  in          { \s -> T_In }
  if          { \s -> T_If }
  then        { \s -> T_Then }
  else         { \s -> T_Else }
  sin          { \s -> T_Sin }
  cos          { \s -> T_Cos }
  exp          { \s -> T_Exp }
  [\+]          { \s -> T_Plus }
  [\-]          { \s -> T_Minus }
  [\*]          { \s -> T_Times }
  [\/]          { \s -> T_Divide }
  [\(]          { \s -> T_LeftPar }
  [\)]          { \s -> T_RightPar }  
  [\=]          { \s -> T_Assign }
  [\<]          { \s -> T_Less }
  [\>]          { \s -> T_Greater }
  \=\=          { \s -> T_Equal }
  [\\]          { \s -> T_Not }
  \&\&          { \s -> T_And }
  \|\|          { \s -> T_Or }
  @number       { \s -> T_Double (read s)}
  $alpha [$alpha $digit \_ \']*   { \s -> T_Id s }  

{
-- Each action has type :: String -> Token

-- The token type:

type Id = String

----------------------------------------------------------------
-- Token type
----------------------------------------------------------------

data Token = T_Double Double
           | T_Id Id
           | T_Plus
           | T_Minus
           | T_Times
           | T_Divide
           | T_LeftPar
           | T_RightPar
           | T_Assign
           | T_Let
           | T_In
           | T_If
           | T_Then
           | T_Else
           | T_Cos
           | T_Sin
           | T_Exp
           | T_Less
           | T_Greater
           | T_Equal
           | T_Not
           | T_And
           | T_Or
           deriving (Eq, Show)

}