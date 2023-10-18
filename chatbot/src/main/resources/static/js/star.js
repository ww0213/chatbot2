const ratingStars = [...document.getElementsByClassName("rating__star")];
const ratingStars2 = [...document.getElementsByClassName("rating__star2")];

function executeRating(stars) {
  const starClassActive = "rating__star fas fa-star";
  const starClassInactive = "rating__star far fa-star";
  let rating = 0; // 初始化評分為0

  stars.map((star, index) => {
    star.onclick = () => {
      if (star.className === starClassInactive) {
        for (let j = 0; j <= index; j++) {
          stars[j].className = starClassActive;
        }
        rating = index + 1; // 設置評分
      } else {
        for (let j = index; j < stars.length; j++) {
          stars[j].className = starClassInactive;
        }
        rating = index; // 設置評分
      }
      document.getElementById('rating').value = rating;

    };
  });
}
executeRating(ratingStars);


function executeRating2(stars) {
  const starClassActive = "rating__star2 fas fa-star";
  const starClassInactive = "rating__star2 far fa-star";
  let rating = 0; // 初始化評分為0

  stars.map((star, index) => {
    star.onclick = () => {
      if (star.className === starClassInactive) {
        for (let j = 0; j <= index; j++) {
          stars[j].className = starClassActive;
        }
        rating = index + 1; // 設置評分
      } else {
        for (let j = index; j < stars.length; j++) {
          stars[j].className = starClassInactive;
        }
        rating = index; // 設置評分
      }
      document.getElementById('rating2').value = rating;

    };
  });
}
executeRating2(ratingStars2);






