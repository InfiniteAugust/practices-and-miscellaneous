-- 6521724 zy21724 Jingyu LUO

type Matrix a = [[a]]

eg1 :: Matrix Int
eg1 = [ [1, 3], 
        [0, 5], 
        [-3, 4], 
        [2, 2] ]

eg2 :: Matrix Int
eg2 = [ [3, 1, 4],
        [-1, 0, 5] ]

eg3 :: Matrix Int 
eg3 = [ [] ]

eg4 :: Matrix Int
eg4 = [ [2],
        [3] ]

eg5 :: Matrix Double
eg5 = [ [6.2, 4.3, 7.4, -7.3], 
        [9.3, 1.2, 0.4, -6.2] ]

-- question 1
empty :: Matrix a -> Bool
empty a | head (map null a) = True
        | otherwise = False 

-- question 2
rowCount :: Matrix a -> Int
rowCount a | empty a = 0
           | otherwise = length a

-- question 3
columnCount :: Matrix a -> Int
columnCount a | empty a = 0
              | otherwise = length (head a)

-- question 4
element' :: Matrix a -> Int -> Int -> a
element' a x y = (a !! x) !! y

-- question 5
element :: Matrix a -> Int -> Int -> Maybe a
element a x y | x < 0 || y < 0 || x >= rowCount a || y >= columnCount a = Nothing 
              | otherwise = Just (element' a x y)

-- qeustion 6
data MatrixErr = NegIndex
               | RowTooLarge
               | ColTooLarge
               deriving Show

elementEth :: Matrix a -> Int -> Int -> Either MatrixErr a
elementEth a x y | x < 0 || y < 0 = Left NegIndex
                 | x >= rowCount a = Left RowTooLarge
                 | y >= columnCount a = Left ColTooLarge
                 | otherwise = Right (element' a x y)

-- question 7
identity :: Int -> Maybe (Matrix Int)
identity x | x <= 0 = Nothing
           | otherwise = Just (mat x)
mat n = [ [fromEnum (i == j) | i <- [1..n]] | j <- [1..n] ]

-- question 8
addMat :: Num a => Matrix a -> Matrix a -> Maybe (Matrix a)
addMat m n | rowCount m /= rowCount n || columnCount m /= columnCount n = Nothing
           | empty m = Nothing 
           | otherwise = Just (sum'' m n)
sum'' m n = [ sum' p q | (p, q) <- zip m n ]  -- add two matrice 
sum' xs ys = [ x + y | (x, y) <- zip xs ys ]  -- add two lists 

-- question 9
mapMat :: (a -> b) -> Matrix a -> Matrix b
mapMat f [[]] = [[]]
mapMat f m    = map (map f) m

-- question 10 
compress :: Num a => Matrix a -> Matrix a
compress [[]]   = [[]]
compress []     = []
compress (x:xs) = [sum x] : compress xs